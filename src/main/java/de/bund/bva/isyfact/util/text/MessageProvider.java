package de.bund.bva.isyfact.util.text;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.text.MessageFormat.format;

/**
 * Creates formatted messages, using the provided message bundle and keys.
 */
public class MessageProvider {

    public static final ResourceBundle MESSAGE_BUNDLE = ResourceBundle.getBundle("messages", Locale.GERMANY);

    /**
     * Creates a message for the given key and text.
     *
     * @param key  The key for the message.
     * @param text A free text and parameters.
     * @return The created message.
     */

    public static String getMessage(String key, String... text) {
        return format("[{0}] {1}", key, format(MESSAGE_BUNDLE.getString(key), (Object[]) text));
    }


}
