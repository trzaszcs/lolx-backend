package pl.poznan.lolx.infrastructure.add.category

import org.springframework.stereotype.Component
import pl.poznan.lolx.domain.add.Category
import pl.poznan.lolx.domain.add.CategoryDetails

@Component
class CategoryDetailsService implements CategoryDetails {

    private static final List<Category> CATEGORIES = Arrays.asList(
            new Category(id: 1, name: 'Sprzątanie'),
            new Category(id: 7, name: 'Ciało'),
            new Category(id: 4, name: 'Korepetycje'),
            new Category(id: 6, name: 'Mycie auta',),
            new Category(id: 5, name: 'Transport/Zakupy'),
            new Category(id: 3, name: 'Opieka nad zwierzętami'),
            new Category(id: 2, name: 'Ogród'),
            new Category(id: 9, name: 'Złota rączka'),
            new Category(id: 10, name: 'Przeprowadzka'),
            new Category(id: 8, name: 'Inne')
    )

    @Override
    Optional<Category> find(String id) {
        Optional.ofNullable(CATEGORIES.find { it.id == id })
    }
}
