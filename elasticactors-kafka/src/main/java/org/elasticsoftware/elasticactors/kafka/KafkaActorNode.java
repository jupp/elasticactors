package org.elasticsoftware.elasticactors.kafka;

import com.google.common.collect.ImmutableList;
import org.elasticsoftware.elasticactors.ActorNode;
import org.elasticsoftware.elasticactors.ActorRef;
import org.elasticsoftware.elasticactors.NodeKey;
import org.elasticsoftware.elasticactors.PhysicalNode;
import org.elasticsoftware.elasticactors.cluster.InternalActorSystem;
import org.elasticsoftware.elasticactors.cluster.LocalClusterActorNodeRef;
import org.elasticsoftware.elasticactors.messaging.InternalMessage;
import org.elasticsoftware.elasticactors.messaging.InternalMessageImpl;
import org.elasticsoftware.elasticactors.messaging.internal.CreateActorMessage;
import org.elasticsoftware.elasticactors.serialization.Message;
import org.elasticsoftware.elasticactors.serialization.MessageSerializer;
import org.elasticsoftware.elasticactors.serialization.SerializationContext;

import java.io.IOException;
import java.util.List;

public final class KafkaActorNode implements ActorNode {

    private final NodeKey key;
    private final PhysicalNode node;
    private final KafkaActorThread actorThread;
    private final InternalActorSystem actorSystem;
    private final ActorRef myRef;

    public KafkaActorNode(PhysicalNode node, KafkaActorThread actorThread, InternalActorSystem actorSystem) {
        this.key = new NodeKey(actorSystem.getName(), node.getId());
        this.myRef = new LocalClusterActorNodeRef(actorSystem, actorSystem.getParent().getClusterName(), this);
        this.node = node;
        this.actorThread = actorThread;
        this.actorSystem = actorSystem;
        // if we are local, we need to ensure the actorThread will add our Topic
        if(node.isLocal()) {
            actorThread.assign(this);
        }
    }

    @Override
    public NodeKey getKey() {
        return key;
    }

    @Override
    public boolean isLocal() {
        return node.isLocal();
    }

    @Override
    public ActorRef getActorRef() {
        return myRef;
    }

    @Override
    public void sendMessage(ActorRef sender, ActorRef receiver, Object message) throws Exception {
        sendMessage(sender, ImmutableList.of(receiver), message);
    }

    @Override
    public void sendMessage(ActorRef sender, List<? extends ActorRef> receivers, Object message) throws Exception {
        offerInternalMessage(createInternalMessage(sender, receivers, message));
    }

    @Override
    public void undeliverableMessage(InternalMessage message, ActorRef receiverRef) throws Exception {
        InternalMessage undeliverableMessage = new InternalMessageImpl( receiverRef,
                message.getSender(),
                message.getPayload(),
                message.getPayloadClass(),
                message.isDurable(),
                true,
                message.getTimeout());
        offerInternalMessage(undeliverableMessage);
    }

    @Override
    public void offerInternalMessage(InternalMessage internalMessage) {
        // the calling thread can be a KafkaActorThread (meaning this is called as a side effect of handling another message)
        // or it is called from another thread in which case it is not part of an existing transaction
        actorThread.send(key, internalMessage);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() {

    }

    /**
     * This is a special case, a TempActor can only be created locally and should not create a message on the cluster topic
     *
     * @param createActorMessage
     */
    void createTempActor(CreateActorMessage createActorMessage) {
        this.actorThread.createTempActor(createActorMessage);
    }

    private InternalMessage createInternalMessage(ActorRef from, List<? extends ActorRef> to, Object message) throws IOException {
        MessageSerializer<Object> messageSerializer = (MessageSerializer<Object>) actorSystem.getSerializer(message.getClass());
        if(messageSerializer == null) {
            throw new IllegalArgumentException("MessageSerializer not found for message of type "+message.getClass().getName());
        }
        // get the durable flag
        Message messageAnnotation = message.getClass().getAnnotation(Message.class);
        final boolean durable = (messageAnnotation != null) && messageAnnotation.durable();
        final int timeout = (messageAnnotation != null) ? messageAnnotation.timeout() : Message.NO_TIMEOUT;
        return new InternalMessageImpl(from, ImmutableList.copyOf(to), SerializationContext.serialize(messageSerializer, message),message.getClass().getName(),durable, timeout);
    }
}
