package jewellery.jewellery.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequest {

    private Integer orderId;
    private Double orderTotalSum;
}