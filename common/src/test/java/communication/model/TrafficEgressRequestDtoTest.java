package communication.model;

import static org.instancio.Select.field;

import communication.model.base.BaseDtoTest;
import communication.model.base.Dto;
import java.io.IOException;
import java.util.List;
import org.instancio.Instancio;
import org.msgpack.core.MessageUnpacker;

class TrafficEgressRequestDtoTest extends BaseDtoTest {

  @Override
  protected List<TrafficEgressRequestDto> createDtos() {
    return Instancio.ofList(TrafficEgressRequestDto.class)
        .set(field(Dto::getIdentifier), TrafficEgressRequestDto.IDENTIFIER)
        .create();
  }

  @Override
  protected Dto createResult(final MessageUnpacker messageUnpacker) throws IOException {
    return new TrafficEgressRequestDto(messageUnpacker);
  }
}
