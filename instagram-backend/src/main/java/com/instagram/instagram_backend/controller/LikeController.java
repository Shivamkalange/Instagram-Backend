//package com.instagram.instagram_backend.controller;
//
//
//import com.instagram.instagram_backend.service.LikeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/likes")
//public class LikeController {
//
//      @Autowired
//     private LikeService likeService;
//
//      @PostMapping("{username}/{postId}")
//      public ResponseEntity<?> likePost(String username, Long postId){
//          try{
//              boolean liked = likeService.likePost(username, postId);
//              if(liked){
//                  return new ResponseEntity<>("Post liked successfully", HttpStatusCode.valueOf(200));
//              }
//              else{
//                  return new ResponseEntity<>("Post unliked successfully", HttpStatusCode.valueOf(200));
//              }
//          } catch (RuntimeException e) {
//              throw new RuntimeException(e);
//          }
//      }
//
//}
