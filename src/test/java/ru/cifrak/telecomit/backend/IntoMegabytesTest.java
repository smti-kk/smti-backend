package ru.cifrak.telecomit.backend;

import org.junit.jupiter.api.Test;
import ru.cifrak.telecomit.backend.utils.Converter;
import ru.cifrak.telecomit.backend.utils.IpReversed;

import static org.assertj.core.api.Assertions.assertThat;

public class IntoMegabytesTest {
    @Test
    public void IpWithoutMaskGetIpTest() {
        assertThat(Converter.megabytes(15411791894L)).isEqualTo("14 697,830");
    }
}
