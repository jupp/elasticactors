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

package org.elasterix.elasticactors.messaging;

/**
 * @author Joost van de Wijgerd
 */
public interface MessageQueue {
    boolean offer(InternalMessage message);

    boolean add(InternalMessage message);

    InternalMessage poll();

    String getName();

    void initialize() throws Exception;

    void destroy();

    InternalMessage peek();

    MessageHandler getMessageHandler();

    MessageHandlerEventListener getMessageHandlerEventListener();
}
