package org.remast.baralga.gui.settings;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserSettingsTest {

    @Test
    void getEmptyUser() {
        // Arrange
        Properties applicationConfig = new Properties();
        UserSettings userSettings = UserSettings.instance();
        userSettings.setUserConfig(applicationConfig);

        // Act
        String user = userSettings.getUser();

        // Assert
        assertNull(user);
    }

    @Test
    void getUserFromProps() {
        // Arrange
        String systemUser = "mylogin";
        Properties userConfig = new Properties();
        userConfig.setProperty("user", systemUser);
        UserSettings userSettings = UserSettings.instance();
        userSettings.setUserConfig(userConfig);

        // Act
        String user = UserSettings.instance().getUser();

        // Assert
        assertEquals(user, systemUser);
    }

    @Test
    void getPasswordFromProps() {
        // Arrange
        String configuredPassword = "**sshh**";
        Properties userConfig = new Properties();
        userConfig.setProperty("password", configuredPassword);
        UserSettings userSettings = UserSettings.instance();
        userSettings.setUserConfig(userConfig);

        // Act
        String password = UserSettings.instance().getPassword();

        // Assert
        assertEquals(password, configuredPassword);
    }
}
