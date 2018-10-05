package com.lolisapp.gametraductor.bean;



public class User {

    private Integer avance;
    private Long userLocalId;
    private Long userExternId;
    private String dni;
    private String email;
    private String lastName;
    private String firstName;
    private String Phone;
    private Integer codeDepartamento;
    private Integer codeProvincia;
    private Integer codeDistrito;
    private String password;

    private String institution;
    private String region;
    private Integer isMember;

    public Integer getAvance() {
        return avance;
    }

    public void setAvance(Integer avance) {
        this.avance = avance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserLocalId() {

        return userLocalId;
    }



    public void setUserLocalId(Long userLocalId) {
        this.userLocalId = userLocalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Integer getCodeDepartamento() {
        return codeDepartamento;
    }

    public void setCodeDepartamento(Integer codeDepartamento) {
        this.codeDepartamento = codeDepartamento;
    }

    public Integer getCodeProvincia() {
        return codeProvincia;
    }

    public void setCodeProvincia(Integer codeProvincia) {
        this.codeProvincia = codeProvincia;
    }

    public Integer getCodeDistrito() {
        return codeDistrito;
    }

    public void setCodeDistrito(Integer codeDistrito) {
        this.codeDistrito = codeDistrito;
    }

    public Long getUserExternId() {
        return userExternId;
    }

    public void setUserExternId(Long userExternId) {
        this.userExternId = userExternId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getIsMember() {
        return isMember;
    }

    public void setIsMember(Integer isMember) {
        this.isMember = isMember;
    }
}