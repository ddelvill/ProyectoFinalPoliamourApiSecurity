package com.example.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.entities.Post;
import com.example.entities.User;
import com.example.model.FileUploadResponse;
import com.example.services.PostService;
import com.example.services.UserService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")

public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private FileDownloadUtil fileDownloadUtil;

    @Autowired
    private UserService userService;

    
//METODO FINDALL 

    @GetMapping
    public ResponseEntity<List<Post>> findAll(@RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        ResponseEntity<List<Post>> responseEntity = null;

        List<Post> posts= new ArrayList<>();

        Sort sortById = Sort.by("id");

        if (page != null && size != null) {

            try {
                Pageable pageable = PageRequest.of(page, size, sortById);
                Page<Post> postsPaginados = postService.findAll(pageable);
                posts= postsPaginados.getContent();
                responseEntity = new ResponseEntity<List<Post>>(posts, HttpStatus.OK);

            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {

            try {

                posts = postService.findAll(sortById);

                responseEntity = new ResponseEntity<List<Post>>(posts, HttpStatus.OK);

            } catch (Exception e) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;

    }


    //IMAGENES Y CREADO
    @PostMapping(consumes = "multipart/form-data")
    @Transactional
    public ResponseEntity<Map<String, Object>> insert(@Valid 
                @RequestPart(name = "post") Post post, 
                BindingResult result,
                @RequestPart(name = "filePost", required = false) MultipartFile filePost) throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();

        ResponseEntity<Map<String, Object>> responseEntity = null;

        if (result.hasErrors()) {

            List<String> errorMessages = new ArrayList<>();

            for (ObjectError error : result.getAllErrors()) {

                errorMessages.add(error.getDefaultMessage());

            }

            responseAsMap.put("errores", errorMessages);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

            return responseEntity;

        }

        if(!filePost.isEmpty()) {
            String fileCode = fileUploadUtil.saveFile(filePost.getOriginalFilename(), filePost);
            post.setImagePost(fileCode + "-" + filePost.getOriginalFilename());

            FileUploadResponse fileUploadResponse = FileUploadResponse
                        .builder()
                        .fileName(fileCode + "-" + filePost.getOriginalFilename())
                        .downloadURI("/posts/downloadFile/" + fileCode + "-" + filePost.getOriginalFilename())
                        .size(filePost.getSize())
                        .build();

    

            responseAsMap.put("info de la imagen: ", fileUploadResponse);
        }

        Post postDB = postService.save(post);

        try {

            if (postDB != null) {

                String message = "El post se ha creado correctamente";
                responseAsMap.put("mensaje", message);
                responseAsMap.put("post", postDB);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);

            } else {

                String message = "El post no se ha creado correctamente";

                responseAsMap.put("mensaje", message);

                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);

            }

        } catch (DataAccessException e) {

            String errorGrave = "Ha tenido lugar un error grave  y, la causa más problable puede ser: "
                    + e.getMostSpecificCause();

            responseAsMap.put("errorGrave", errorGrave);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return responseEntity;
    }


    //BUSCAR POR ID USER -> BETTER SI LO PONEMOS POR NOMBRE 
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findByUserId(@PathVariable(name = "id") Integer id) {

        ResponseEntity<Map<String, Object>> responseEntity = null;

        Map<String, Object> responseAsMap = new HashMap<>();

        try {

            List<Post> post = postService.findByUserId(id);

            if (post != null) {
                String successMessage = "Se ha encontrado el Post del usuario con id: " + id + " correctamente";
                responseAsMap.put("mensaje", successMessage);
                responseAsMap.put("post", post);
                // responseAsMap.put("mascotas", cliente.getMascotas());
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.OK);

            } else {

                String errorMessage = "No se ha encontrado el post del usuario con id: " + id;
                responseAsMap.put("error", errorMessage);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {

            String errorGrave = "Error grave";
            responseAsMap.put("error", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return responseEntity;
    }    

    //ACTUALIZACION = RESOLVER PERMISOS 

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> update(
            @Valid @RequestPart(name = "post") Post post,
            BindingResult result, 
            @RequestPart(name = "filePost", required = false) MultipartFile filePost,
         
            @RequestPart(name = "user", required = false) User user,

            @PathVariable(name = "id") Integer id) throws IOException {

        Map<String, Object> responseAsMap = new HashMap<>();
        ResponseEntity<Map<String, Object>> responseEntity = null;

        if (result.hasErrors()) {

            List<String> errorMessages = new ArrayList<>();

            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            responseAsMap.put("errores", errorMessages);

            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
            return responseEntity;
        }

        if(!filePost.isEmpty()) {
            String fileCode = fileUploadUtil.saveFile(filePost.getOriginalFilename(), filePost);
            post.setImagePost(fileCode + "-" + filePost.getOriginalFilename());

            FileUploadResponse fileUploadResponse = FileUploadResponse
                        .builder()
                        .fileName(fileCode + "-" + filePost.getOriginalFilename())
                        .downloadURI("/posts/downloadFile/" + fileCode + "-" + filePost.getOriginalFilename())
                        .size(filePost.getSize())
                        .build();

            responseAsMap.put("info de la imagen: ", fileUploadResponse);
        }

        post.setId(id);
        Post postDB = postService.save(post);

        
        try {

            if (postDB != null) {


                if(user!= null) {

                        userService.save(user);
                        postDB.setUser(user);
                    }

              

                String message = "El post se ha actualizado correctamente";
                responseAsMap.put("mensaje", message);
                responseAsMap.put("post", postDB);
                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.CREATED);

            } else {

                String errorMensaje = "El post no se ha actualizado correctamente";

                responseAsMap.put("mensaje", errorMensaje);

                responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,
                        HttpStatus.INTERNAL_SERVER_ERROR);               

            }

        } catch (DataAccessException e) {
            String errorGrave = "Se ha producido un error grave y la causa puede ser " + e.getMostSpecificCause();
            responseAsMap.put("errorGrave", errorGrave);
            responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }

    //BORRADO

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable(name = "id") Integer id) {
        ResponseEntity<String> responseEntity = null;

        try {

            Post post = postService.findbyId(id);

            if (post != null) {
   
                postService.delete(post);
                responseEntity = new ResponseEntity<String>("Post borrado exitosamente", HttpStatus.OK);
            } else {
  
                responseEntity = new ResponseEntity<String>("Post no encontrado", HttpStatus.NOT_FOUND);
            }

        } catch (DataAccessException e) {
            e.getMostSpecificCause();
            responseEntity = new ResponseEntity<String>("Error fatal", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

        

    }

    
    //ESTO NO ES NECESARIO 
    @GetMapping("/downloadFile/{fileCode}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "fileCode") String fileCode) {

        Resource resource = null;

        try {
            resource = fileDownloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found ", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        .body(resource);

    }  


}
    

