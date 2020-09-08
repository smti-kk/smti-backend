package ru.cifrak.telecomit.backend;

import org.junit.jupiter.api.Test;
import ru.cifrak.telecomit.backend.utils.IpReversed;

import static org.assertj.core.api.Assertions.assertThat;

public class IpReversedTest {
    private final String ip = "192.168.0.1";
    private final String ipWithMask = "192.168.0.1/30";

    @Test
    public void IpWithoutMaskGetIpTest() {
        assertThat(new IpReversed(ip).ip()).isEqualTo("1.0.168.192");
    }

    @Test
    public void IpWithoutMaskGetMaskTest() {
        assertThat(new IpReversed(ip).mask()).isEqualTo("32");
    }

    @Test
    public void IpWithMaskGetIpTest() {
        assertThat(new IpReversed(ipWithMask).ip()).isEqualTo("1.0.168.192");
    }

    @Test
    public void IpWithMaskGetIpStraitTest() {
        assertThat(new IpReversed(ipWithMask).ipStrait()).isEqualTo("192.168.0.1");
    }

    @Test
    public void IpWithMaskGetMaskTest() {
        assertThat(new IpReversed(ipWithMask).mask()).isEqualTo("30");
    }
}
