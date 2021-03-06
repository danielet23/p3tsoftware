/**
 *   Copyright 2008-2009, 2011 INRIA, Université Pierre Mendès France
 *   
 *   TripleBasedEntitySim.java is part of OntoSim.
 *
 *   OntoSim is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   OntoSim is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with OntoSim; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package fr.inrialpes.exmo.ontosim.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.wcohen.ss.Jaccard;

import fr.inrialpes.exmo.ontosim.Measure;
import fr.inrialpes.exmo.ontosim.entity.model.Entity;
import fr.inrialpes.exmo.ontosim.entity.triplebased.IterativeNodeSim;
import fr.inrialpes.exmo.ontosim.string.StringMeasureSS;
import fr.inrialpes.exmo.ontowrap.LoadedOntology;
import fr.inrialpes.exmo.ontowrap.OntowrapException;

/**
 * class TripleBasedEntitySim
 */
public class TripleBasedEntitySim implements Measure<Entity<OntResource>> {
	final static Logger logger = LoggerFactory.getLogger(TripleBasedEntitySim.class);

    IterativeNodeSim currentSim;
    Collection<Model> currentModels;
    Measure<String> ssim = new StringMeasureSS(new Jaccard());

    public TripleBasedEntitySim() {
    }
    
    public TripleBasedEntitySim(Measure<String> ssim) {
	this.ssim=ssim;
    }

    public double getDissim(Entity<OntResource> e1, Entity<OntResource> e2) {
	return 1- getMeasureValue(e1,e2);
    }

    public fr.inrialpes.exmo.ontosim.Measure.TYPES getMType() {
	return TYPES.similarity;
    }

    public void forCicleO1( LoadedOntology<OntResource> o1,Set<String> o1URIS){
		try {
			for (Object o : o1.getEntities()) {

                    o1URIS.add(o1.getEntityURI(o).toString());
			}
		} catch (OntowrapException e) {
			logger.error("FATAL error", e);
		}
	}

	public void forCicle02(LoadedOntology<OntResource> o2, Set<String> o2URIS){
		try {
			for (Object o : o2.getEntities()) {

				o2URIS.add(o2.getEntityURI(o).toString());
			}
		} catch (OntowrapException e) {
			logger.error("FATAL error", e);
		}
	}

    public double getMeasureValue(Entity<OntResource> e1, Entity<OntResource> e2) {
	OntModel e1Model = e1.getObject().getOntModel();
	OntModel e2Model = e2.getObject().getOntModel();
	if (currentModels==null || !(currentModels.contains(e1Model) && currentModels.contains(e2Model))) {
	    LoadedOntology<OntResource> o1 =  e1.getOntology();
	    LoadedOntology<OntResource> o2 =  e2.getOntology();
		Set<String> o1URIS = new HashSet<String>();
	    Set<String> o2URIS = new HashSet<String>();

		this.forCicleO1(o1,o1URIS);

		this.forCicle02(o2,o1URIS);

		currentSim = new IterativeNodeSim(e1Model,o1URIS,
		    				e2Model,o2URIS,
		    				ssim,1);
	    currentModels = currentSim.getModels();
	}
	double val = currentSim.getMeasureValue(e1.getObject().asNode(), e2.getObject().asNode());
	return Double.isNaN(val)?0:val;
    }

    public double getSim(Entity<OntResource> e1, Entity<OntResource> e2) {
	return getMeasureValue(e1,e2);
    }

}
