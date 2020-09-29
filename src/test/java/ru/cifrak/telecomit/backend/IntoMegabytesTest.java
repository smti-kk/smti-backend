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
    @Test
    public void stringTest(){
        System.out.println(
                "telecom:org:2550:Сухобузимский район, Сухобузимское с., Селезнева ул., 46:ap:3491:Селезнева ул., 46".matches("telecom:org:"+2550+":.+:ap:"+3491+":.+")
        );
    }
}
