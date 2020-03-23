package lk.kmartsuper.kmartsuper.util.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AbstractController<E, I> {

    //1. findAll method create.
    //2. addForm method create.
    //3. persist method create.
    //4. edit method create.
    //5. delete method create.
    //6. view details.

    String findAll();

    String addForm(I id);

    String persist(E e);

    String edit(I id);

    String delete(I id);

    String view(I id);
}