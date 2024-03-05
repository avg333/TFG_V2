package basestation;

import algorithm.AlgorithmMode;
import communication.ClientCommunicator;
import communication.ClientCommunicatorTCP;
import communication.ClientCommunicatorUDP;
import communication.CommunicatorMode;
import domain.Position;
import picocli.CommandLine;

public class BaseStationFactory {

  public BaseStation createBaseStation(final String[] args) {
    final BaseStationConfigDto config = createConfigDto(args);

    return new BaseStation(
        createClientCommunicator(config),
        new Position(config.getPositionX(), config.getPositionY()),
        createBaseConfig(config));
  }

  private BaseStationConfigDto createConfigDto(String[] args) {
    final BaseStationConfigDto config = new BaseStationConfigDto();
    new CommandLine(config).execute(args);
    return config;
  }

  private BaseStationConfig createBaseConfig(BaseStationConfigDto config) {
    return new BaseStationConfig(
        AlgorithmMode.fromCode(config.getAlgorithmMode()),
        config.getProcessingCapacity(),
        config.getTimeToOff(),
        config.getTimeToOn(),
        config.getTimeHysteresis(),
        config.getAlgorithmParam());
  }

  private ClientCommunicator createClientCommunicator(BaseStationConfigDto config) {
    return CommunicatorMode.fromCode(config.getCommunicatorMode()) == CommunicatorMode.TCP
        ? new ClientCommunicatorTCP(config.getBrokerIp(), config.getBrokerPort())
        : new ClientCommunicatorUDP(config.getBrokerIp(), config.getBrokerPort());
  }
}
