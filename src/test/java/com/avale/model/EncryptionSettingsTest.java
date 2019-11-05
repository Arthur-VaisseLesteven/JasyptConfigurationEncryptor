package com.avale.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
public class EncryptionSettingsTest {

    public static final String MASTER_PASSWORD = "password";

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
}