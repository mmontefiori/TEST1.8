package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws Exception {
		new Test().test07();
	}

	private void test01() {
		List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD", "CCC");

		Map<String, String> map = list.stream().distinct()
				.collect(Collectors.toMap(elem -> elem, elem -> elem.toLowerCase()));
		System.out.println(map);
	}

	private void test02() {

		List<Book> list = Arrays.asList(new Book("title 1", "author 1"), new Book("title 2", "author 2"));

		Map<String, String> map = list.stream().collect(Collectors.toMap(Book::getTitle, Book::getAuthor));
		System.out.println(map);
	}

	private void test03() {

		List<Art> list = Arrays.asList(new Art("MUSIC", 15), new Art("PAINTING", 2), new Art("THEATRE", 7),
				new Art("LITERATURE", 10));

		EnumMap<EArt, Integer> enMap = list.stream()
				.collect(Collectors.toMap(b -> EArt.valueOf(b.getArt()), b -> b.getIndex(), (x, y) -> {
					return x;
				}, () -> new EnumMap<>(EArt.class)));
		System.out.println(enMap);
	}

	private void test04() {

		List<Long> list = new ArrayList<>();
		for (long k = 0; k < 100; k++) {
			list.add(k);
		}

		System.out.println("START1: " + new Date());
		Vector<Long> v = new Vector<>();
		list.parallelStream().forEach(k -> {
			v.add(k);
		});
		System.out.println("END1: " + new Date());

		System.out.println(v.size());

		System.out.println("START2: " + new Date());
		List<Long> res = list.parallelStream().map(this::doSomething).collect(Collectors.toList());
		System.out.println("END2: " + new Date());

		System.out.println(res.size());

		System.out.println("START3: " + new Date());
		List<Long> ls = new ArrayList<>();
		list.parallelStream().forEach(k -> {
			ls.add(k);
		});
		System.out.println("END3: " + new Date());

		System.out.println(ls.size());
	}

	private Long doSomething(Long k) {
		return k + 1;
	}

	private void test05() throws Exception {
		List<Book> books = new ArrayList<>();
		for (int k = 1; k < 100; k++) {
			Book book = new Book("TITLE_" + k, "AUTHOR_" + k);
			books.add(book);
		}

		AtomicInteger counter = new AtomicInteger(0);
		Collection<List<Book>> subList = books.stream()
				.collect(Collectors.groupingBy(s -> counter.getAndIncrement() / 5)).values();

		System.out.println(subList.size());
	}

	private void test06() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		List<Book> books = new ArrayList<>();
		for (int k = 1; k < 10; k++) {
			Book book = new Book("TITLE_" + k, "AUTHOR_" + k);
			books.add(book);
		}

		int chunckSize = 0;
		List<Book> splitItem = new ArrayList<>();
		List<List<Book>> split = new ArrayList<List<Book>>();
		for (Book book : books) {
			int itemSize = mapper.writeValueAsBytes(book).length;
			if (chunckSize + itemSize > 100) {
				split.add(new ArrayList<>(splitItem));
				splitItem.clear();
				chunckSize = 0;
			}
			chunckSize += itemSize;
			splitItem.add(book);
		}

		System.out.println(split.size());
	}

	private void test07() {
		Date dt = new Date(0);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);

		System.out.println(dt.equals(cal.getTime()) && cal.getTime().getTime() == 0);
	}

}
