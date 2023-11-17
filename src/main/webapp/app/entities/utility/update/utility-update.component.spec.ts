import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { UtilityService } from '../service/utility.service';
import { IUtility } from '../utility.model';
import { UtilityFormService } from './utility-form.service';

import { UtilityUpdateComponent } from './utility-update.component';

describe('Utility Management Update Component', () => {
  let comp: UtilityUpdateComponent;
  let fixture: ComponentFixture<UtilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilityFormService: UtilityFormService;
  let utilityService: UtilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UtilityUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(UtilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilityFormService = TestBed.inject(UtilityFormService);
    utilityService = TestBed.inject(UtilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const utility: IUtility = { id: 456 };

      activatedRoute.data = of({ utility });
      comp.ngOnInit();

      expect(comp.utility).toEqual(utility);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtility>>();
      const utility = { id: 123 };
      jest.spyOn(utilityFormService, 'getUtility').mockReturnValue(utility);
      jest.spyOn(utilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utility }));
      saveSubject.complete();

      // THEN
      expect(utilityFormService.getUtility).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilityService.update).toHaveBeenCalledWith(expect.objectContaining(utility));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtility>>();
      const utility = { id: 123 };
      jest.spyOn(utilityFormService, 'getUtility').mockReturnValue({ id: null });
      jest.spyOn(utilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utility: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utility }));
      saveSubject.complete();

      // THEN
      expect(utilityFormService.getUtility).toHaveBeenCalled();
      expect(utilityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtility>>();
      const utility = { id: 123 };
      jest.spyOn(utilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
