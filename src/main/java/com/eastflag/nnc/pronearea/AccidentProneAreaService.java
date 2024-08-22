package com.eastflag.nnc.pronearea;

import com.eastflag.nnc.common.CommonResponse;
import com.eastflag.nnc.common.ResponseMessage;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccidentProneAreaService {
    private final AccidentProneAreaRepository accidentProneAreaRepository;
    private final Gson gson = new Gson();
    @Autowired
    public AccidentProneAreaService(AccidentProneAreaRepository accidentProneAreaRepository) {
        this.accidentProneAreaRepository = accidentProneAreaRepository;
    }

    public CommonResponse findAll(String query) {
        ArrayList<AccidentProneArea> result = new ArrayList<>();
        System.out.println("query : " + query);
        try {
            List<AccidentProneArea> accidentProneAreas = accidentProneAreaRepository.findAll();
            Type coordinatesType = new TypeToken<Coordinates>() {}.getType();
            Coordinates coordinates = gson.fromJson(query, coordinatesType);

            System.out.println("coordinates : " + coordinates.toString());
            for(Coordinate coordinate : coordinates.getCoordinates()) {
                for(AccidentProneArea accidentProneArea : accidentProneAreas) {
                    double distance = DistanceCalculator.calculate(coordinate.getLongitude(),coordinate.getLatitude(),
                            accidentProneArea.getLongitude(), accidentProneArea.getLatitude()) * 1000;

                    if(distance < 30 && !result.contains(accidentProneArea)){
                        result.add(accidentProneArea);
                    }
                }
            }

            System.out.println("result : " + result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return CommonResponse.builder()
                .code(200)
                .message(ResponseMessage.SUCCESS)
                .data(result)
                .build();
    }
}
