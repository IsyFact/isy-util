/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.bund.bva.isyfact.util.messages;

/**
 * Diese Klasse implementiert einen Provider für Texte, der zum Laden der Nachrichten den
 * {@link MessageSourceHolder} verwendet. Letztere muss als Spring-Bean in der Anwendung konfiguriert sein.
 */
public class MessageSourceMessageProvider {

    /**
     * Returns the message to the given key.
     *
     * @param schluessel The key.
     * @param parameter  Optional parameters.
     * @return The message to the given key.
     */
    public String getMessage(String schluessel, String... parameter) {
        return MessageSourceHolder.getMessage(schluessel, parameter);
    }
}
