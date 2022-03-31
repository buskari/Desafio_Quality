package com.mercadolivre.desafio_quality;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mercadolivre.desafio_quality.model.dto.DistrictDTO;
import com.mercadolivre.desafio_quality.model.dto.GroundDTO;
import com.mercadolivre.desafio_quality.model.dto.RoomInputDTO;
import com.mercadolivre.desafio_quality.repository.GroundRepository;
import com.mercadolivre.desafio_quality.repository.RoomRepository;
import com.mercadolivre.desafio_quality.service.DistrictService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.mercadolivre.desafio_quality.model.District;
import com.mercadolivre.desafio_quality.model.Ground;
import com.mercadolivre.desafio_quality.model.Room;
import com.mercadolivre.desafio_quality.model.dto.RoomDTO;
import com.mercadolivre.desafio_quality.service.GroundService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GroundServiceTest {

    private GroundService service;
    private final GroundRepository groundRepositoryMock = Mockito.mock(GroundRepository.class);
    private final RoomRepository roomRepositoryMock = Mockito.mock(RoomRepository.class);
    private final DistrictService districtServiceMock = Mockito.mock(DistrictService.class);


    @BeforeEach
    public void init() {
        service = new GroundService(groundRepositoryMock, roomRepositoryMock,  districtServiceMock);
    }

    @Test
    void shouldReturnRoomArea() {


    }

    private RoomDTO mockRoomDTO() {
        return new RoomDTO("any_name", 10.0);
    }

    //	private Ground mockGround() {
//		return new Ground(
//			"any_name",
//			mockDistrict(),
//			mockRoomList()
//		);
//	}
//
//	private District mockDistrict() {
//		return new District(
//			"any_name",
//			BigDecimal.valueOf(1000)
//		);
//	}
//
//	private List<Room> mockRoomList() {
//
//	}
    public Ground createGround() {
        Room roomQuarto = Room.builder().roomName("Quarto").roomLength(Double.valueOf(10)).roomWidth(Double.valueOf(5)).build();
        Room roomSala = Room.builder().roomName("Sala").roomLength(Double.valueOf(5)).roomWidth(Double.valueOf(5)).build();

        List<Room> rooms = new ArrayList();
        rooms.add(roomQuarto);
        rooms.add(roomSala);

        District district = new District("melhor distrito", BigDecimal.valueOf(1200));

        return Ground.builder().propName("Casa Chique").rooms(rooms).district(district).build();
    }

    @Test
    public void verificaSeRetornaValorDoImovel() {
        Ground casaChique = createGround();
        Mockito.when(groundRepositoryMock.findById(Long.valueOf("1234"))).thenReturn(Optional.of(casaChique));

        BigDecimal result = service.groundValue(Long.valueOf("1234"));

        assertEquals(BigDecimal.valueOf(90000.0), result);
    }

    @Test
    public void verificaSeRetornaErrorAoConsultarValorDeImovelInexistente() {

        Mockito.when(groundRepositoryMock.findById(Long.valueOf("1234"))).thenReturn(Optional.empty());

        Throwable exception = assertThrows(RuntimeException.class,()-> service.groundValue(Long.valueOf("1234")));
        assertEquals("imovel nao encontrado na nossa base de dados!", exception.getMessage() );
    }

    @Test
    public void verificaSeRetornaMaiorArea() {

        Ground ground = createGround();
        List<RoomDTO> roomDTOS= new ArrayList<>();
        for (Room room: ground.getRooms()) {
            roomDTOS.add(new RoomDTO(room.getRoomName(), Double.valueOf(room.getRoomLength() * room.getRoomWidth())));
        }
        RoomDTO roomDTO = service.biggestRoom(roomDTOS);
        assertEquals(roomDTOS.get(0), roomDTO);
    }


    @Test
	public void verificaSeSalvaUmRoom() {
        Ground ground = createGround();
        DistrictDTO districtDTO = new DistrictDTO(ground.getDistrict().getPropDistrict(), ground.getDistrict().getValueDistrictM2());
        List<RoomInputDTO> roomInputDTO = new ArrayList<>();

        for (Room room: ground.getRooms()) {
            roomInputDTO.add(new RoomInputDTO(room.getRoomName(),room.getRoomLength(), room.getRoomWidth()));
        }

        GroundDTO groundDTO = new GroundDTO(ground.getPropName(), districtDTO, roomInputDTO);

        Mockito.when(roomRepositoryMock.saveAll(Mockito.anyList())).thenReturn(ground.getRooms());
        Mockito.when(districtServiceMock.save(Mockito.any())).thenReturn(ground.getDistrict());
        Mockito.when(groundRepositoryMock.save(Mockito.any())).thenReturn(ground);

        Ground save = service.save(groundDTO);

        assertEquals(ground, save);

    }
}
