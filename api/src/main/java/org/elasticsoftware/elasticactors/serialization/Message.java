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

package org.elasticsoftware.elasticactors.serialization;

import java.lang.annotation.*;

/**
 * @author Joost van de Wijgerd
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Message {
    int NO_TIMEOUT = -1;
    String DEFAULT_TYPE = "CLASSNAME";
    String DEFAULT_VERSION = "1";

    /**
     * Determines which framework will be used to serialize and deserialize this message
     *
     * @return
     */
    Class<? extends SerializationFramework> serializationFramework();

    /**
     * If a message is durable it will always be put on the Message layer. Depending on the implementation of the underlying
     * Messaging fabric the message will also be made durable in the queue. If a message is not durable, messages sent to
     * {@link org.elasticsoftware.elasticactors.ElasticActor}s that live in the same JVM are not send to to Messaging layer
     * but are delivered directly (on a seperate thread).
     *
     * Non durable messages for {@link org.elasticsoftware.elasticactors.ElasticActor}s that reside in a remote JVM are
     * not persisted to disc in the Message layer and thus take up fewer resources.
     *
     * @return  whether this message is durable (defaults to true)
     */
    boolean durable() default true;

    /**
     * If a message is marked as immutable, the framework can implement several optimizations. Most notably: messages that
     * are forwarded or messages that are send to multiple {@link org.elasticsoftware.elasticactors.ElasticActor}s are not
     * only serialized once.
     *
     * @return  whether this message is immutable (defaults to false)
     */
    boolean immutable() default false;

    /**
     * Determines how long a message stays queued up in the underlying messaging service. If it isn't consumed within
     * {@link #timeout()} seconds it will not be delivered to the receiver.
     *
     * This is useful for cases where the ActorSystem gets overloaded and can't keep up. Defaults to -1 (NO_TIMEOUT)
     *
     * @return  the timeout value (in milliseconds) for the message pojo annotation with this annotation
     */
    int timeout() default NO_TIMEOUT;

    String type() default DEFAULT_TYPE;

    String version() default DEFAULT_VERSION;

}
