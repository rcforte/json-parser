package com.rcforte.parser.json;

@JsonClass
public class Person {

  @JsonField(mappedTo = "name")
  private String name;

  @JsonField(mappedTo = "age")
  private Integer age;

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
        '}';
  }
}
