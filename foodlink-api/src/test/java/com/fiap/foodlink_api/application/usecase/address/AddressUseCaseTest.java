package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Casos de uso de endereco")
class AddressUseCaseTest {

	@Test
	@DisplayName("Deve criar endereco")
	void deveCriarEndereco() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.save(any(Address.class))).thenReturn(new Address(
				id,
				"Rua das Flores",
				"123",
				null,
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				OffsetDateTime.now()
		));
		CreateAddressUseCase useCase = new CreateAddressUseCase(gateway);

		Address address = useCase.execute("Rua das Flores", "123", null, "Centro", "Sao Paulo", "SP", "01001000");

		assertEquals(id, address.getId());
		assertEquals("Rua das Flores", address.getStreet());
		verify(gateway).save(any(Address.class));
	}

	@Test
	@DisplayName("Deve normalizar endereco antes de criar")
	void deveNormalizarEnderecoAntesDeCriar() {
		AddressGateway gateway = mock(AddressGateway.class);
		when(gateway.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
		CreateAddressUseCase useCase = new CreateAddressUseCase(gateway);

		Address address = useCase.execute("  Rua das Flores  ", "  123  ", "   ", "  Centro  ", "  Sao Paulo  ", " sp ", " 01001000 ");

		assertNull(address.getId());
		assertEquals("Rua das Flores", address.getStreet());
		assertEquals("123", address.getNumber());
		assertNull(address.getComplement());
		assertEquals("Centro", address.getDistrict());
		assertEquals("Sao Paulo", address.getCity());
		assertEquals("SP", address.getState());
		assertEquals("01001000", address.getZipCode());
	}

	@Test
	@DisplayName("Deve buscar endereco por identificador")
	void deveBuscarEnderecoPorIdentificador() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		Address savedAddress = criarEndereco(id);
		when(gateway.findById(id)).thenReturn(Optional.of(savedAddress));
		GetAddressByIdUseCase useCase = new GetAddressByIdUseCase(gateway);

		Address address = useCase.execute(id);

		assertEquals(id, address.getId());
		assertEquals("Rua das Flores", address.getStreet());
		verify(gateway).findById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar endereco inexistente")
	void deveLancarExcecaoAoBuscarEnderecoInexistente() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		GetAddressByIdUseCase useCase = new GetAddressByIdUseCase(gateway);

		AddressNotFoundException exception = assertThrows(
				AddressNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Endereco nao encontrado: " + id, exception.getMessage());
		verify(gateway).findById(id);
	}

	@Test
	@DisplayName("Deve listar enderecos cadastrados")
	void deveListarEnderecosCadastrados() {
		AddressGateway gateway = mock(AddressGateway.class);
		when(gateway.findAll()).thenReturn(List.of(criarEndereco(UUID.randomUUID()), criarEndereco(UUID.randomUUID())));
		ListAddressesUseCase useCase = new ListAddressesUseCase(gateway);

		List<Address> addresses = useCase.execute();

		assertEquals(2, addresses.size());
		verify(gateway).findAll();
	}

	@Test
	@DisplayName("Deve atualizar endereco")
	void deveAtualizarEndereco() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		Address savedAddress = criarEndereco(id);
		when(gateway.findById(id)).thenReturn(Optional.of(savedAddress));
		when(gateway.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateAddressUseCase useCase = new UpdateAddressUseCase(gateway);
		ArgumentCaptor<Address> captor = ArgumentCaptor.forClass(Address.class);

		Address address = useCase.execute(id, "Avenida Brasil", "500", "Sala 10", "Jardins", "Rio de Janeiro", "RJ", "20000000");

		assertEquals(id, address.getId());
		assertEquals("Avenida Brasil", address.getStreet());
		verify(gateway).findById(id);
		verify(gateway).save(captor.capture());
		assertEquals("Avenida Brasil", captor.getValue().getStreet());
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar endereco inexistente")
	void deveLancarExcecaoAoAtualizarEnderecoInexistente() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		UpdateAddressUseCase useCase = new UpdateAddressUseCase(gateway);

		AddressNotFoundException exception = assertThrows(
				AddressNotFoundException.class,
				() -> useCase.execute(id, "Rua das Flores", "123", null, "Centro", "Sao Paulo", "SP", "01001000")
		);

		assertEquals("Endereco nao encontrado: " + id, exception.getMessage());
		verify(gateway, never()).save(any(Address.class));
	}

	@Test
	@DisplayName("Deve remover endereco cadastrado")
	void deveRemoverEnderecoCadastrado() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.existsById(id)).thenReturn(true);
		DeleteAddressUseCase useCase = new DeleteAddressUseCase(gateway);

		useCase.execute(id);

		verify(gateway).existsById(id);
		verify(gateway).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover endereco inexistente")
	void deveLancarExcecaoAoRemoverEnderecoInexistente() {
		AddressGateway gateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.existsById(id)).thenReturn(false);
		DeleteAddressUseCase useCase = new DeleteAddressUseCase(gateway);

		AddressNotFoundException exception = assertThrows(
				AddressNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Endereco nao encontrado: " + id, exception.getMessage());
		verify(gateway).existsById(id);
		verify(gateway, never()).deleteById(id);
	}

	private Address criarEndereco(UUID id) {
		return new Address(
				id,
				"Rua das Flores",
				"123",
				null,
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				OffsetDateTime.now()
		);
	}
}
