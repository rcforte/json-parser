package com.rcforte.parser.json;

import java.util.List;

@JsonClass
public class Person {

  @JsonField(mappedTo = "myName")
  private String name;

  @JsonField(mappedTo = "myAge")
  private Integer age;

  @JsonField(mappedTo = "myFriend")
  private Person friend;

  @JsonField(mappedTo = "myNickNames")
  private List<String> nickNames;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Person{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", friend=" + friend +
        ", nickNames=" + nickNames +
        '}';
  }
}
