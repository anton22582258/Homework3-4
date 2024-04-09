package ru.hogwarts.school3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school3.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int min, int max);

}
