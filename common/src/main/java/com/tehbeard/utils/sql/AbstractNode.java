package com.tehbeard.utils.sql;

/**
 *
 * @author James
 */
public abstract class AbstractNode {
    
    private AbstractNode parent;
    private int id;
    private int depth;
    private String path;

    private String encode_path(){
        AbstractNode current = this;
        StringBuilder path = new StringBuilder();
        this.depth = 0;
        while(current != null){
            String _id = Integer.toString(current.id,36);
            path.insert(0,Integer.toString(_id.length()-1,36) + _id);
            current = current.parent;
            this.depth ++;
        }
        return path.toString();
    }
}


//@Table("testrows")
//public class TestRow {
//    @Id(autoGen=true)
//    private int id;
//    @Index(name="idx")
//    @Field(name="",canNull=false)
//    private String name;
//
//    @Foreign(canNull=false)
//    private User user;
//}