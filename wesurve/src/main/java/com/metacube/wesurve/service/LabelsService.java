/**
 * The LabelsService interface is service layer interface Labels Model 
 */
package com.metacube.wesurve.service;

import com.metacube.wesurve.model.Labels;

public interface LabelsService {
	Labels getLabelByLabelName(String label);
}