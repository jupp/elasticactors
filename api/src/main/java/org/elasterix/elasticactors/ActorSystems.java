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

package org.elasterix.elasticactors;

import org.elasterix.elasticactors.cluster.ActorRefFactory;
import org.elasterix.elasticactors.serialization.MessageDeserializer;
import org.elasterix.elasticactors.serialization.MessageSerializer;

/**
 * @author Joost van de Wijgerd
 */
public interface ActorSystems {
    String getClusterName();

    ActorSystem get(String actorSystemName);

    <T> MessageSerializer<T> getSystemMessageSerializer(Class<T> messageClass);

    <T> MessageDeserializer<T> getSystemMessageDeserializer(Class<T> messageClass);

    ActorRefFactory getActorRefFactory();
}