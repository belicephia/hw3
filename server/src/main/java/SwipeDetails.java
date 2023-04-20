import java.util.Objects;

public class SwipeDetails {
    private String swiper;
    private String swipee;



    private String comments;

    public boolean isLikeornot() {
        return likeornot;
    }

    public void setLikeornot(boolean likeornot) {
        this.likeornot = likeornot;
    }

    private boolean likeornot;

    public String getSwiper() {
        return swiper;
    }

    public void setSwiper(String swiper) {
        this.swiper = swiper;
    }

    public String getSwipee() {
        return swipee;
    }

    public void setSwipee(String swipee) {
        this.swipee = swipee;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "SwipeDetails{" +
                "swiper='" + swiper + '\'' +
                ", swipee='" + swipee + '\'' +
                ", comments='" + comments + '\'' +
                ", likeornot=" + likeornot +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwipeDetails that = (SwipeDetails) o;
        return Objects.equals(swiper, that.swiper) && Objects.equals(swipee, that.swipee) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(swiper, swipee, comments);
    }
}
