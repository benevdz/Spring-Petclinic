package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owner.PetController;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetType;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
@DisplayName("PetController Tests")
class PetControllerTest {

	@Mock
	private OwnerRepository ownerRepository;

	@InjectMocks
	private PetController petController;

	@BeforeEach
	void setup() {
	}

	@Test
	@DisplayName("Test populatePetTypes")
	void testPopulatePetTypes() {
		PetType type1 = new PetType();
		type1.setId(1);
		type1.setName("Dog");

		PetType type2 = new PetType();
		type2.setId(2);
		type2.setName("Cat");

		doReturn(Arrays.asList(type1, type2)).when(ownerRepository).findPetTypes();

		Collection<PetType> petTypes = petController.populatePetTypes();

		assertThat(petTypes).hasSize(2);
		assertThat(petTypes).contains(type1, type2);
		verify(ownerRepository).findPetTypes();
	}

	@Test
	@DisplayName("Test findOwner throws IllegalArgumentException")
	void testFindOwnerThrowsIllegalArgumentException() {
		int ownerId = 999;
		doThrow(new IllegalArgumentException()).when(ownerRepository).findById(ownerId);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			petController.findOwner(ownerId);
		});

		verify(ownerRepository).findById(ownerId);
	}

	@Test
	@DisplayName("Test findPet with null petId returns new Pet")
	void testFindPetWithNullPetIdReturnsNewPet() {
		int ownerId = 1;
		Pet pet = petController.findPet(ownerId, null);
		assertThat(pet).isNotNull();
	}

	@Test
	@DisplayName("Test processCreationForm handles duplicate pet name")
	void testProcessCreationFormHandlesDuplicatePetName() {
		Owner owner = org.mockito.Mockito.mock(Owner.class);
		Pet existingPet = new Pet();
		existingPet.setName("Buddy");
		owner.addPet(existingPet);

		Pet newPet = new Pet();
		newPet.setName("Buddy");

		BindingResult result = org.mockito.Mockito.mock(BindingResult.class);
		ModelMap model = org.mockito.Mockito.mock(ModelMap.class);
		RedirectAttributes redirectAttributes = org.mockito.Mockito.mock(RedirectAttributes.class);

		doReturn(existingPet).when(owner).getPet("Buddy", true);
		doReturn(true).when(result).hasErrors();

		petController.processCreationForm(owner, newPet, result, model, redirectAttributes);

		verify(result).rejectValue("name", "duplicate", "already exists");
		verify(result).hasErrors();
		verify(model).put("pet", newPet);
	}

}
