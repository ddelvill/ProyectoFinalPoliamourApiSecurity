package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.entities.Department;
import com.example.entities.Post;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.services.DepartmentService;
import com.example.services.PostService;
import com.example.services.UserService;
import com.example.services.YardService;

@SpringBootApplication
public class ProyectoFinalPoliamourApiApplication  implements CommandLineRunner{

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private YardService yardService;

	@Autowired
	private PostService postService;





	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalPoliamourApiApplication.class, args);


		
	

}

	@Override
	public void run(String... args) throws Exception {
		
		departmentService.save(Department.builder()
			.name("Informatica")
			.build());
		
		departmentService.save(Department.builder()
			.name("RRHH")
			.build());

		Yard yard1 =  yardService.save(Yard.builder()
			.id(1)
			.name("Yard1")
			.department(departmentService.findbyId(1))
			.build());
		
		Yard yard2 = yardService.save(Yard.builder()
			.id(2)
			.name("Yard2")
			.department(departmentService.findbyId(1))
			.build());

		Yard yard3 = yardService.save(Yard.builder()
			.id(3)
			.name("Yard3")
			.department(departmentService.findbyId(1))
			.build());

		List<Yard> listaYard1 = new ArrayList<>();
		listaYard1.add(yard1);
		listaYard1.add(yard2);

		List<Yard> listaYard2 = new ArrayList<>();
		listaYard2.add(yard3);

		List<String> hobbiesUser1 = new ArrayList<>();
		hobbiesUser1.add("baloncesto");
		hobbiesUser1.add("lectura");

		
		userService.save(User.builder()
			.id(1)
			.name("Marina")
			.surnames("Giner")
			.email("marinaginer@gmail.com")
			.password("password1")
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.yards(listaYard1)
			.hobbie(hobbiesUser1)
			.phone("677888999")
			.build());

		List<String> hobbiesUser2 = new ArrayList<>();
		hobbiesUser2.add("futbol");
		hobbiesUser2.add("lectura");

		userService.save(User.builder()
			.id(2)
			.name("Paloma")
			.surnames("Galan")
			.email("palomagalan@gmail.com")
			.password("password2")
			.city("Valencia")
			.department(departmentService.findbyId(2))
			.hobbie(hobbiesUser2)
			.phone("654632981")
			.build());

		List<String> hobbiesUser3 = new ArrayList<>();
		hobbiesUser3.add("senderismo");
		hobbiesUser3.add("equitacion");
		

		userService.save(User.builder()
			.id(3)
			.name("Maria")
			.surnames("Romero")
			.email("mariaromero@gmail.com")
			.password("password3")
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.hobbie(hobbiesUser3)
			.yards(listaYard2)
			.build());

		List<String> hobbiesUser4 = new ArrayList<>();
		hobbiesUser4.add("equitacion");
			
	
		userService.save(User.builder()
			.id(4)
			.name("Alex")
			.surnames("Sanchez")
			.email("alexsanchez@gmail.com")
			.password("password3")				
			.city("Murcia")
			.department(departmentService.findbyId(1))
			.hobbie(hobbiesUser4)				
			.yards(listaYard2)
			.build());	

			

		
		userService.save(User.builder()
			.id(5)
			.name("Sheila")
			.surnames("Nuñez")
			.email("sheilanuñez@gmail.com")
			.password("password1")
			.city("Valencia")
			.department(departmentService.findbyId(2))
			.phone("677888999")
			.build());

		postService.save(Post.builder()
			.id(1)
			.text("Hola, esto es un post.")
			.user(userService.findbyId(1))
			.build());
		






		
			
	}
}
