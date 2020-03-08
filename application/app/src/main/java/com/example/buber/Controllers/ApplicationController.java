package com.example.buber.Controllers;

import com.example.buber.App;
import com.example.buber.Model.ApplicationModel;
import com.example.buber.Model.Trip;
import com.example.buber.Model.User;
import com.example.buber.Model.UserLocation;
import com.example.buber.Services.ApplicationService;
import com.example.buber.Views.UIErrorHandler;

import java.util.List;

public class ApplicationController {
    private ApplicationModel model;

    public ApplicationController(ApplicationModel model) {
        this.model = model;
    }

    public void createNewUser(String username,
                              String password,
                              String firstName,
                              String lastName,
                              String email,
                              String phoneNumber,
                              User.TYPE type,
                              UIErrorHandler view) {
        ApplicationService.createNewUser(
                username,
                password,
                firstName,
                lastName,
                email,
                phoneNumber,
                type,
                (resultData, err) -> {
                    if (err != null) view.onError(err);
                    else {
                        User u = (User) resultData.get("user");
                        model.setSessionUser(u);
                    }
                }
        );
    }

    public void login(String email, String password, User.TYPE type, UIErrorHandler view) {
        ApplicationService.loginUser(email, password, type, (resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                User u = (User) resultData.get("user");
                model.setSessionUser(u);
            }
        });
    }

    public void logout() {
        ApplicationService.logoutUser();
        model.setSessionUser(null);
    }

    public void updateUserLocation(UserLocation l) {
        ApplicationModel m = App.getModel();
        if (m.getSessionUser() !=  null) {
            m.getSessionUser().setCurrentUserLocation(l);
            m.notifyObservers();
        }
    }

    public static void getTripsForUser(UIErrorHandler view) {
        ApplicationModel m = App.getModel();
        ApplicationService.getFilterTrips((resultData, err) -> {
            if (err != null) view.onError(err);
            else {
                List<Trip> sessionTripList = (List) resultData.get("trip-list");
                m.setSessionTripList(sessionTripList);
            }
        });
    }
}
