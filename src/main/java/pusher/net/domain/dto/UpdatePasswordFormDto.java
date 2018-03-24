package pusher.net.domain.dto;

public class UpdatePasswordFormDto {
    private String newPass;
    private String repeatPass;
    private String email;

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getRepeatPass() {
        return repeatPass;
    }

    public void setRepeatPass(String repeatPass) {
        this.repeatPass = repeatPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
