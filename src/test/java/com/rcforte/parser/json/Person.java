package com.rcforte.parser.json;

import java.util.List;

@JsonClass
public class Person {

  @JsonField(mappedTo = "myName")
  String name;

  @JsonField(mappedTo = "myAge")
  Integer age;

  @JsonField(mappedTo = "myFriend")
  Person friend;

  @JsonField(mappedTo = "myNickNames")
  List<Object> nickNames;

  public void name(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  public void age(Integer age) {
    this.age = age;
  }

  public Integer age() {
    return age;
  }

  public void friend(Person friend) {
    this.friend = friend;
  }

  public Person friend() {
    return friend;
  }

  public void nickNames(List<Object> nickNames) {
    this.nickNames = nickNames;
  }

  public List<Object> nickNames() {
    return nickNames;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    var p = (Person) o;
    return (p.name == null && name == null) || (p.name != null && p.name.equals(name))
        && (p.age == null && age == null) || (p.age != null && p.age.equals(age))
        && (p.friend == null && friend == null) || (p.friend != null && p.friend.equals(friend))
        && (p.nickNames == null & nickNames == null) || (p.nickNames != null && p.nickNames.equals(nickNames));
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
