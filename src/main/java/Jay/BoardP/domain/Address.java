package Jay.BoardP.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address {


    //이방법보단 일대다  , 단방향 양방향 매핑을 고려해라

    private String city;

    private String street;

    private String zidCode;


    protected Address() {

    }

    public Address(String city, String street, String zidCode) {
        this.city = city;
        this.street = street;
        this.zidCode = zidCode;
    }
}
