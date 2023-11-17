import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IHousehold } from 'app/entities/household/household.model';
import { HouseholdService } from 'app/entities/household/service/household.service';
import { IUtilityProvider } from 'app/entities/utility-provider/utility-provider.model';
import { UtilityProviderService } from 'app/entities/utility-provider/service/utility-provider.service';
import { IHouseholdUtility } from '../household-utility.model';
import { HouseholdUtilityService } from '../service/household-utility.service';
import { HouseholdUtilityFormService } from './household-utility-form.service';

import { HouseholdUtilityUpdateComponent } from './household-utility-update.component';

describe('HouseholdUtility Management Update Component', () => {
  let comp: HouseholdUtilityUpdateComponent;
  let fixture: ComponentFixture<HouseholdUtilityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let householdUtilityFormService: HouseholdUtilityFormService;
  let householdUtilityService: HouseholdUtilityService;
  let householdService: HouseholdService;
  let utilityProviderService: UtilityProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), HouseholdUtilityUpdateComponent],
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
      .overrideTemplate(HouseholdUtilityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HouseholdUtilityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    householdUtilityFormService = TestBed.inject(HouseholdUtilityFormService);
    householdUtilityService = TestBed.inject(HouseholdUtilityService);
    householdService = TestBed.inject(HouseholdService);
    utilityProviderService = TestBed.inject(UtilityProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Household query and add missing value', () => {
      const householdUtility: IHouseholdUtility = { id: 456 };
      const household: IHousehold = { id: 26617 };
      householdUtility.household = household;

      const householdCollection: IHousehold[] = [{ id: 9847 }];
      jest.spyOn(householdService, 'query').mockReturnValue(of(new HttpResponse({ body: householdCollection })));
      const additionalHouseholds = [household];
      const expectedCollection: IHousehold[] = [...additionalHouseholds, ...householdCollection];
      jest.spyOn(householdService, 'addHouseholdToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ householdUtility });
      comp.ngOnInit();

      expect(householdService.query).toHaveBeenCalled();
      expect(householdService.addHouseholdToCollectionIfMissing).toHaveBeenCalledWith(
        householdCollection,
        ...additionalHouseholds.map(expect.objectContaining),
      );
      expect(comp.householdsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UtilityProvider query and add missing value', () => {
      const householdUtility: IHouseholdUtility = { id: 456 };
      const utilityProvider: IUtilityProvider = { id: 4168 };
      householdUtility.utilityProvider = utilityProvider;

      const utilityProviderCollection: IUtilityProvider[] = [{ id: 2767 }];
      jest.spyOn(utilityProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: utilityProviderCollection })));
      const additionalUtilityProviders = [utilityProvider];
      const expectedCollection: IUtilityProvider[] = [...additionalUtilityProviders, ...utilityProviderCollection];
      jest.spyOn(utilityProviderService, 'addUtilityProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ householdUtility });
      comp.ngOnInit();

      expect(utilityProviderService.query).toHaveBeenCalled();
      expect(utilityProviderService.addUtilityProviderToCollectionIfMissing).toHaveBeenCalledWith(
        utilityProviderCollection,
        ...additionalUtilityProviders.map(expect.objectContaining),
      );
      expect(comp.utilityProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const householdUtility: IHouseholdUtility = { id: 456 };
      const household: IHousehold = { id: 13475 };
      householdUtility.household = household;
      const utilityProvider: IUtilityProvider = { id: 23889 };
      householdUtility.utilityProvider = utilityProvider;

      activatedRoute.data = of({ householdUtility });
      comp.ngOnInit();

      expect(comp.householdsSharedCollection).toContain(household);
      expect(comp.utilityProvidersSharedCollection).toContain(utilityProvider);
      expect(comp.householdUtility).toEqual(householdUtility);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHouseholdUtility>>();
      const householdUtility = { id: 123 };
      jest.spyOn(householdUtilityFormService, 'getHouseholdUtility').mockReturnValue(householdUtility);
      jest.spyOn(householdUtilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ householdUtility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: householdUtility }));
      saveSubject.complete();

      // THEN
      expect(householdUtilityFormService.getHouseholdUtility).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(householdUtilityService.update).toHaveBeenCalledWith(expect.objectContaining(householdUtility));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHouseholdUtility>>();
      const householdUtility = { id: 123 };
      jest.spyOn(householdUtilityFormService, 'getHouseholdUtility').mockReturnValue({ id: null });
      jest.spyOn(householdUtilityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ householdUtility: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: householdUtility }));
      saveSubject.complete();

      // THEN
      expect(householdUtilityFormService.getHouseholdUtility).toHaveBeenCalled();
      expect(householdUtilityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHouseholdUtility>>();
      const householdUtility = { id: 123 };
      jest.spyOn(householdUtilityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ householdUtility });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(householdUtilityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareHousehold', () => {
      it('Should forward to householdService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(householdService, 'compareHousehold');
        comp.compareHousehold(entity, entity2);
        expect(householdService.compareHousehold).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUtilityProvider', () => {
      it('Should forward to utilityProviderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(utilityProviderService, 'compareUtilityProvider');
        comp.compareUtilityProvider(entity, entity2);
        expect(utilityProviderService.compareUtilityProvider).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
