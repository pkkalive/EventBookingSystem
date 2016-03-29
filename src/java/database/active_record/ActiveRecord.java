/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.active_record;

/**
 *
 * @author Purushotham
 */
public interface ActiveRecord {
    
    void insert() throws Exception;
    void update() throws Exception;
    void delete() throws Exception;
    
}
