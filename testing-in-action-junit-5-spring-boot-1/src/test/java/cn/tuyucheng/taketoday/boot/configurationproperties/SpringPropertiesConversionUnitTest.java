package cn.tuyucheng.taketoday.boot.configurationproperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.unit.DataSize;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = PropertyConversion.class)
@ContextConfiguration(classes = CustomCredentialsConverter.class)
@TestPropertySource("classpath:spring-conversion-test.properties")
class SpringPropertiesConversionUnitTest {
    @Autowired
    private PropertyConversion propertyConversion;

    @Test
    @DisplayName("whenUsingSpringDefaultSizeConversion_thenDataSizeObjectIsSet")
    void whenUsingSpringDefaultSizeConversion_thenDataSizeObjectIsSet() {
        assertEquals(DataSize.ofMegabytes(500), propertyConversion.getUploadSpeed());
        assertEquals(DataSize.ofGigabytes(10), propertyConversion.getDownloadSpeed());
    }

    @Test
    @DisplayName("whenUsingSpringDefaultDurationConversion_thenDurationObjectIsSet")
    void whenUsingSpringDefaultDurationConversion_thenDurationObjectIsSet() {
        assertEquals(Duration.ofDays(1), propertyConversion.getBackupDay());
        assertEquals(Duration.ofHours(8), propertyConversion.getBackupHour());
    }

    @Test
    @DisplayName("whenResisteringCustomCredentialsConverter_thenCredentialsAreParsed")
    void whenResisteringCustomCredentialsConverter_thenCredentialsAreParsed() {
        assertEquals("user", propertyConversion.getCredentials().getUsername());
        assertEquals("123", propertyConversion.getCredentials().getPassword());
    }
}