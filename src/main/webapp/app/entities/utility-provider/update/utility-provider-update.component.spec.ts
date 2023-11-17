import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUtility } from 'app/entities/utility/utility.model';
import { UtilityService } from 'app/entities/utility/service/utility.service';
import { UtilityProviderService } from '../service/utility-provider.service';
import { IUtilityProvider } from '../utility-provider.model';
import { UtilityProviderFormService } from './utility-provider-form.service';

import { UtilityProviderUpdateComponent } from './utility-provider-update.component';

describe('UtilityProvider Management Update Component', () => {
  let comp: UtilityProviderUpdateComponent;
  let fixture: ComponentFixture<UtilityProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let utilityProviderFormService: UtilityProviderFormService;
  let utilityProviderService: UtilityProviderService;
  let utilityService: UtilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UtilityProviderUpdateComponent],
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
      .overrideTemplate(UtilityProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UtilityProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    utilityProviderFormService = TestBed.inject(UtilityProviderFormService);
    utilityProviderService = TestBed.inject(UtilityProviderService);
    utilityService = TestBed.inject(UtilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Utility query and add missing value', () => {
      const utilityProvider: IUtilityProvider = { id: 456 };
      const utility: IUtility = { id: 29308 };
      utilityProvider.utility = utility;

      const utilityCollection: IUtility[] = [{ id: 10948 }];
      jest.spyOn(utilityService, 'query').mockReturnValue(of(new HttpResponse({ body: utilityCollection })));
      const additionalUtilities = [utility];
      const expectedCollection: IUtility[] = [...additionalUtilities, ...utilityCollection];
      jest.spyOn(utilityService, 'addUtilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ utilityProvider });
      comp.ngOnInit();

      expect(utilityService.query).toHaveBeenCalled();
      expect(utilityService.addUtilityToCollectionIfMissing).toHaveBeenCalledWith(
        utilityCollection,
        ...additionalUtilities.map(expect.objectContaining),
      );
      expect(comp.utilitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const utilityProvider: IUtilityProvider = { id: 456 };
      const utility: IUtility = { id: 15419 };
      utilityProvider.utility = utility;

      activatedRoute.data = of({ utilityProvider });
      comp.ngOnInit();

      expect(comp.utilitiesSharedCollection).toContain(utility);
      expect(comp.utilityProvider).toEqual(utilityProvider);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilityProvider>>();
      const utilityProvider = { id: 123 };
      jest.spyOn(utilityProviderFormService, 'getUtilityProvider').mockReturnValue(utilityProvider);
      jest.spyOn(utilityProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilityProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilityProvider }));
      saveSubject.complete();

      // THEN
      expect(utilityProviderFormService.getUtilityProvider).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(utilityProviderService.update).toHaveBeenCalledWith(expect.objectContaining(utilityProvider));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilityProvider>>();
      const utilityProvider = { id: 123 };
      jest.spyOn(utilityProviderFormService, 'getUtilityProvider').mockReturnValue({ id: null });
      jest.spyOn(utilityProviderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilityProvider: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: utilityProvider }));
      saveSubject.complete();

      // THEN
      expect(utilityProviderFormService.getUtilityProvider).toHaveBeenCalled();
      expect(utilityProviderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUtilityProvider>>();
      const utilityProvider = { id: 123 };
      jest.spyOn(utilityProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ utilityProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(utilityProviderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUtility', () => {
      it('Should forward to utilityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(utilityService, 'compareUtility');
        comp.compareUtility(entity, entity2);
        expect(utilityService.compareUtility).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
