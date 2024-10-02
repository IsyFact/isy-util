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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestMessageSourceMessageProvider.TestConfig.class)
public class TestMessageSourceMessageProvider {

    @Test
    public void testGetMessage() {
        MessageSourceMessageProvider provider = new MessageSourceMessageProvider();
        assertNotNull(provider);

        String result = provider.getMessage("message2", "Hans");
        assertEquals("Hallo Hans", result);

        result = MessageSourceHolder.getMessage("message1");
        assertEquals("Hallo Welt", result);

    }

    @Test
    public void testGetMessageWrongMessageCode() {
        MessageSourceMessageProvider provider = new MessageSourceMessageProvider();
        assertNotNull(provider);

        String result = provider.getMessage("message3", (String[]) null);
        assertEquals("message3", result);

        result = provider.getMessage("message3");
        assertEquals("message3", result);

        result = provider.getMessage("message3", "Hans", "Mustermann");
        assertEquals("message3: Hans, Mustermann", result);
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("locale/messages");
            return messageSource;
        }

        @Bean
        public MessageSourceHolder messageSourceHolder(MessageSource messageSource) {
            MessageSourceHolder holder = new MessageSourceHolder();
            holder.setMessageSource(messageSource);
            return holder;
        }
    }
}
