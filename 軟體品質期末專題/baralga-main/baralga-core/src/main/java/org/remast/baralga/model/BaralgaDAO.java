package org.remast.baralga.model;

import org.remast.baralga.model.filter.Filter;
import org.remast.baralga.repository.ActivityVO;
import org.remast.baralga.repository.BaralgaRepository;
import org.remast.baralga.repository.FilterVO;
import org.remast.baralga.repository.ProjectVO;
import org.remast.baralga.repository.file.BaralgaFileRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Reads and writes all objects to the database and maintains the database connection.
 * @author remast
 */
public class BaralgaDAO {

	final BaralgaRepository repository;

	public BaralgaDAO(final BaralgaRepository repository) {
		this.repository = repository;
	}

	public void initialize() {
		repository.initialize();
	}

	/**
	 * Closes the database by closing the only connection to it.
	 */
	public void close() {
		repository.close();
	}

	/**
	 * Removes a project.
	 * @param project the project to remove
	 */
	public void remove(final Project project) {
		if (project == null) {
			return;
		}

		repository.remove(project.toVO());
	}

	/**
	 * Adds a new project.
	 * @param project the project to add
	 */
	public void addProject(final Project project) {
		if (project == null) {
			return;
		}

		ProjectVO projectVO = repository.addProject(project.toVO());
		project.setId(projectVO.getId());
	}

	/**
	 * Getter for all projects (both active and inactive).
	 * @return read-only view of the projects
	 */
	public List<Project> getAllProjects() {
		return repository.getAllProjects().stream()
				.map(Project::new)
				.collect(Collectors.toList());
	}

	/**
	 * Check if project administration allowed.
	 */
	public boolean isProjectAdministrationAllowed() {
		return repository.isProjectAdministrationAllowed();
	}

	/**
	 * Provides all activities.
	 * @return read-only view of the activities
	 */
	public List<ProjectActivity> getActivities() {
		return getActivities(null);
	}

	/**
	 * Adds a new activity.
	 * @param activity the activity to add
	 */
	public ProjectActivity addActivity(final ProjectActivity activity) {
		if (activity == null) {
			return null;
		}

		final ActivityVO activityVO = repository.addActivity(activity.toVO());
		return new ProjectActivity(activityVO);
	}

	/**
	 * Removes an activity.
	 * @param activity the activity to remove
	 */
	public void removeActivity(final ProjectActivity activity) {
		if (activity == null) {
			return;
		}

		repository.removeActivity(activity.toVO());
	}
	
	/**
	 * Adds a bunch of projects.
	 * @param projects the projects to add
	 */
	public void addProjects(final Collection<Project> projects) {
		if (projects == null || projects.size() == 0) {
			return;
		}
		
		for (Project project : projects) {
			addProject(project);
		}
	}

	/**
	 * Adds a bunch of activities.
	 * @param activities the activities to add
	 */
	public Collection<ProjectActivity> addActivities(final Collection<ProjectActivity> activities) {
		if (activities == null || activities.size() == 0) {
			return activities;
		}

		return activities.stream()
				.map(this::addActivity)
				.collect(Collectors.toList());
	}

	/**
	 * Removes a bunch of activities.
	 * @param activities the activities to remove
	 */
	public void removeActivities(final Collection<ProjectActivity> activities) {
		if (activities == null || activities.size() == 0) {
			return;
		}

		for (ProjectActivity activity : activities) {
			removeActivity(activity);
		}
	}

	/**
	 * Updates the project in the database. Pending changes will be made persistent.
	 * @param project the project to update
	 */
	public void updateProject(final Project project) {
		if (project == null) {
			return;
		}

		repository.updateProject(project.toVO());
	}

	/**
	 * Updates the activity in the database. Pending changes will be made persistent.
	 * @param activity the activity to update
	 */
	public void updateActivity(final ProjectActivity activity) {
		if (activity == null) {
			return;
		}

		repository.updateActivity(activity.toVO());
	}

	/**
	 * Find a project by it's id.
	 * @param projectId the id of the project
	 * @return the project with the given id or <code>null</code> if there is none
	 */
	public Project findProjectById(final String projectId) {
		if (projectId == null) {
			return null;
		}

		Optional<ProjectVO> projectVO = repository.findProjectById(projectId);
		if (!projectVO.isPresent()) {
			return null;
		}

		return new Project(projectVO.get());
	}

	/**
	 * Provides all activities satisfying the given filter.
	 * @param filter the filter for activities
	 * @return read-only view of the activities
	 */
	public List<ProjectActivity> getActivities(final Filter filter) {
		FilterVO filterVO = filter != null ? filter.toVO() : null;
		return repository.getActivities(filterVO).stream()
				.map(ProjectActivity::new)
				.collect(Collectors.toList());
	}
	
	/**
	 * Gathers some statistics about the tracked activities.
	 */
	public void gatherStatistics() {
		repository.gatherStatistics();
	}

	/**
	 * Removes all projects and activities from the database.
	 */
	public void clearData() {
		repository.clearData();
	}

}
