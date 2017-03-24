/*
 * Copyright 2013 - 2017 The Original Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticsoftware.elasticactors;

import org.elasticsoftware.elasticactors.serialization.SerializationFramework;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Collection;

/**
 * @author Joost van de Wijgerd
 */
public abstract class TypedActor<T> implements ElasticActor<T> {
    private final SubscriberRef SUBSCRIBER_INSTANCE = new SubscriberRef();

    @Override
    public void postCreate(ActorRef creator) throws Exception {
        // do nothing by default
    }

    @Override
    public ActorState preActivate(String previousVersion, String currentVersion, byte[] serializedForm, SerializationFramework serializationFramework) throws Exception {
        // do nothing by default
        return null;
    }

    @Override
    public void postActivate(String previousVersion) throws Exception {
        // do nothing by default
    }

    @Override
    public void onUndeliverable(ActorRef receiver, Object message) throws Exception {
        // do nothing by default
    }

    @Override
    public void prePassivate() throws Exception {
        // do nothing by default
    }

    @Override
    public void preDestroy(ActorRef destroyer) throws Exception {
        // do nothing by default
    }

    public Subscriber<T> asSubscriber(){
        return SUBSCRIBER_INSTANCE;
    }

    public final class SubscriberRef implements org.reactivestreams.Subscriber<T> {

        private SubscriberRef() {
        }

        @Override
        public void onSubscribe(Subscription s) {
            // start the flow
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T t) {
            throw new UnsupportedOperationException("Delegated to onReceive in this implementation");
        }

        @Override
        public void onError(Throwable t) {
            throw new UnsupportedOperationException("Delegated to onReceive in this implementation");
        }

        @Override
        public void onComplete() {
            // do nothing
        }
    }

    // Provide internal access to state etc
    protected final ActorRef getSelf() {
        return ActorContextHolder.getSelf();
    }

    protected <T extends ActorState> T getState(Class<T> stateClass) {
        return ActorContextHolder.getState(stateClass);
    }

    protected final ActorSystem getSystem() {
        return ActorContextHolder.getSystem();
    }

    protected final Collection<PersistentSubscription> getSubscriptions() {
        return ActorContextHolder.getSubscriptions();
    }

    protected final void unhandled(Object message) {
        //@todo: implement logic for unhandled messages
    }


}
