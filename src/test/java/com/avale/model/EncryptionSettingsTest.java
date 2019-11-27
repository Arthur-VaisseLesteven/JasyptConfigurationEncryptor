package com.avale.model;

import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class EncryptionSettingsTest {
	private static final String MASTER_PASSWORD = "password";
	private static final String ALGORITHM = "PBEWITHMD5ANDDES";

	@Test
    public void static_availablePasswordBasedEncryptionAlgorithms_returnSomething() {
        assertThat(EncryptionSettings.availablePasswordBasedEncryptionAlgorithms()).isNotEmpty();
    }

    @Test
    public void equals_isNullSafe() {
        assertThat(anEncryptionSetting().equals(null)).isFalse();
    }

    private EncryptionSettings anEncryptionSetting() {
        return new EncryptionSettings(anAlgorithm(), MASTER_PASSWORD, 10);
    }

    private String anAlgorithm() {
        return EncryptionSettings.availablePasswordBasedEncryptionAlgorithms().get(0);
    }

    @Test
    public void equals_isOtherClassSafe() {
        assertThat(anEncryptionSetting()).isNotEqualTo("Toto");
    }

    @Test
    public void equals_returnsTrueUponSameInstance() {
        EncryptionSettings encryptionSettings = anEncryptionSetting();
        assertThat(encryptionSettings).isEqualTo(encryptionSettings);
    }

    @Test
    public void equals_returnsTrueUponSameFieldValueInstance() {
        assertThat(anEncryptionSetting()).isEqualTo(anEncryptionSetting());
    }

    @Test
    public void equals_returnsFalseUponAnotherInstanceWithDifferentFieldValues() {
        assertThat(anEncryptionSetting()).isNotEqualTo(new EncryptionSettings(anAlgorithm(), MASTER_PASSWORD, 200));
    }

	@Test
	public void propertiesConstructor_looksForExpectedFields() {
		assertThat(new EncryptionSettings(properties())).isEqualTo(new EncryptionSettings(ALGORITHM, null, 100));
	}

	private Properties properties() {
		Properties properties = new Properties();
		properties.put("encryption.settings.iterations", "100");
		properties.put("encryption.settings.algorithm", ALGORITHM);
		return properties;
	}

	@Test
	public void asProperties() {
		assertThat(new EncryptionSettings(properties()).toProperties()).isEqualTo(properties());
	}

	@Test
	public void asProperties_doNotIncludePassword() {
		assertThat(new EncryptionSettings(ALGORITHM, MASTER_PASSWORD, 100).toProperties()).isEqualTo(properties());
	}
}