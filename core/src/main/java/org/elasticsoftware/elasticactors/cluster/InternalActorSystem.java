/*
 * Copyright 2013 Joost van de Wijgerd
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

package org.elasticsoftware.elasticactors.cluster;

import org.elasticsoftware.elasticactors.*;

import java.util.List;

/**
 * @author Joost van de Wijgerd
 */
public interface InternalActorSystem extends ActorSystem, ActorSystemConfiguration {
    /**
     * Return the singleton instance of an {@link ElasticActor}
     *
     * @param actorRef
     * @param actorClass
     * @return
     */
    ElasticActor getActorInstance(ActorRef actorRef,Class<? extends ElasticActor> actorClass);

    /**
     * Return a service or null if the service was not found
     *
     * @param serviceRef
     * @return
     */
    ElasticActor getServiceInstance(ActorRef serviceRef);

    /**
     * Return the {@link ActorShard} that belongs to the give path.
     *
     * @param actorPath
     * @return
     */
    ActorShard getShard(String actorPath);

    /**
     * Returns a {@link ActorNode} that can be either remote or local
     *
     * @param nodeId
     * @return
     */
    ActorNode getNode(String nodeId);

    /**
     * Returns the local {@link ActorNode}
     *
     * @return
     */
    ActorNode getNode();

    ActorRef tempActorFor(String actorId);

    List<String> getDependencies();
}