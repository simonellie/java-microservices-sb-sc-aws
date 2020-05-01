package com.xantrix.webapp.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.entity.FamAssort;

import com.xantrix.webapp.repository.ItemRepository;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = ArticoliWebServiceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ItemRepositoryTest {
	@Autowired
	private ItemRepository itemRepository;
	
	@Test
	public void A_TestInsArticolo() {
		Item item = new Item("123Test","Articolo di Test",6,1.75,"1");

		FamAssort famAssort = new FamAssort();
		famAssort.setId(1);
		item.setFamAssort(famAssort);
		
		Set<Barcode> Eans = new HashSet<>();
		Eans.add(new Barcode(item, "12345678", "CP"));
		
		item.setBarcode(Eans);
		
		itemRepository.save(item);
		
		assertThat(itemRepository.findByCodArt("123Test"))
		.extracting(Item::getDescrizione)
		.isEqualTo("Articolo di Test");
	}
	
	@Test
	public void B_TestSelByDescrizioneLike() {
		List<Item> items = itemRepository.findByDescrizioneLike("Articolo di Test");
		assertEquals(1, items.size());
	}
	
	@Test
	public void C_TestfindByEan() {
		assertThat(itemRepository.SelByEan("12345678"))
				.extracting(Item::getDescrizione)
				.isEqualTo("Articolo di Test");
	}
	
	@Test
	public void D_TestDelArticolo()	{
		Item articolo = itemRepository.findByCodArt("123Test");
		itemRepository.delete(articolo);
	}
}