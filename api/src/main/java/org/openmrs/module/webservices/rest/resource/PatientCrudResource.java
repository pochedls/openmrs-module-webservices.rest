package org.openmrs.module.webservices.rest.resource;

import org.openmrs.Patient;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.Representation;
import org.openmrs.module.webservices.rest.RequestContext;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.annotation.IncludeProperties;
import org.openmrs.module.webservices.rest.annotation.Resource;

@Resource("patient")
@Handler(supports=Patient.class)
@IncludeProperties(rep = "default", properties = { "birthdate", "names" })
public class PatientCrudResource extends DataDelegatingCrudResource<Patient> {
		
	public PatientCrudResource() {
		super(null);
	}
	
	public PatientCrudResource(Patient patient) {
		super(patient);
	}

	@Override
    public Patient newDelegate() {
	    return new Patient();
    }
	
	@Override
    public Patient saveDelegate() {
	    return Context.getPatientService().savePatient(delegate);
    }

	@Override
	public Patient fromString(String uuid) {
	    return Context.getPatientService().getPatientByUuid(uuid);
	}
	
	@Override
	public void delete(String reason, RequestContext context) throws ResourceDeletionException {
	    Context.getPatientService().voidPatient(delegate, reason);
	}
	
	@Override
	public void purge(RequestContext context) throws ResourceDeletionException {
	    try {
	    	Context.getPatientService().purgePatient(delegate);
	    } catch (Exception ex) {
	    	throw new ResourceDeletionException(ex);
	    }
	}

	public Object listSubResource(String subResourceName, RequestContext context) throws Exception {
		return getPropertyWithRepresentation(subResourceName, context.getRepresentation());
    }
	
	public PersonNameCrudResource createPersonName(SimpleObject post, RequestContext context) throws Exception {
		post.put("person", delegate);
		return (PersonNameCrudResource) new PersonNameCrudResource().create(post, context);
	}
	
}
