package org.omar.recipemanagementsystem.review.entity;

import org.omar.recipemanagementsystem.users.entity.user.UserAccountHref;

public record ReviewResponse(String id, String href, String rating,
    UserAccountHref reviewedBy, String date, String review) {
}
