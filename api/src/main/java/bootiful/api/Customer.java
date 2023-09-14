package bootiful.api;

import org.springframework.data.annotation.Id;

record Customer(@Id Integer id, String name, String email) {
}
