package jewellery.jewellery.CARATO_ONLY;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    private String orderID;
    private int totalItems;
    private String address;
    private String totalPrice;
    private String city;
    private String phone;
    private String name;
    private String orderBy;
    private String comment;
    private long orderDate;
    private String status;
    private Boolean isPaymentDone;
    private List<Products> products;
}