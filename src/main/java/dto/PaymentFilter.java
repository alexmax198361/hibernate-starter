package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class PaymentFilter {

    String firstname;
    String lastname;

}
