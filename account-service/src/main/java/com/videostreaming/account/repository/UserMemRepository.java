package com.videostreaming.account.repository;

import com.videostreaming.account.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//NOT IN USED AFTER CONNECTING REAL DATABASE

@Repository
public class UserMemRepository {

    private static final List<User> users = new ArrayList<>();
    private static AtomicLong idCounter = new AtomicLong(1);

    static{
        users.add(new User(idCounter.getAndIncrement(),"mohan20@gmail.com", "test@pass", "Mohan20", LocalDateTime.now(), LocalDateTime.now()));

    }

    public List<User> findAll(){
        return new ArrayList<>(users);
    }


    public Optional<User> findById(Long id){

        return users.stream()
                .filter(u -> u.getId().equals(id)).findFirst();
    }

    public User save(User user){
        if(user.getId() == null){
            user.setId(idCounter.getAndIncrement());
            user.setCreatedAt(LocalDateTime.now());
        }


        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);

        return user;
    }

    public boolean deleteById(Long id){
        return users.removeIf(u -> u.getId().equals(id));
    }

}
