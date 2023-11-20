import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IHouseholdUtility } from 'app/entities/household-utility/household-utility.model';
import { HouseholdUtilityService } from 'app/entities/household-utility/service/household-utility.service';
import { ConsumptionHistoryService } from '../service/consumption-history.service';
import { IConsumptionHistory } from '../consumption-history.model';
import { ConsumptionHistoryFormService } from './consumption-history-form.service';

import { ConsumptionHistoryUpdateComponent } from './consumption-history-update.component';

describe('ConsumptionHistory Management Update Component', () => {
  let comp: ConsumptionHistoryUpdateComponent;
  let fixture: ComponentFixture<ConsumptionHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let consumptionHistoryFormService: ConsumptionHistoryFormService;
  let consumptionHistoryService: ConsumptionHistoryService;
  let householdUtilityService: HouseholdUtilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ConsumptionHistoryUpdateComponent],
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
      .overrideTemplate(ConsumptionHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConsumptionHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    consumptionHistoryFormService = TestBed.inject(ConsumptionHistoryFormService);
    consumptionHistoryService = TestBed.inject(ConsumptionHistoryService);
    householdUtilityService = TestBed.inject(HouseholdUtilityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call HouseholdUtility query and add missing value', () => {
      const consumptionHistory: IConsumptionHistory = { id: 456 };
      const householdUtility: IHouseholdUtility = { id: 1588 };
      consumptionHistory.householdUtility = householdUtility;

      const householdUtilityCollection: IHouseholdUtility[] = [{ id: 30436 }];
      jest.spyOn(householdUtilityService, 'query').mockReturnValue(of(new HttpResponse({ body: householdUtilityCollection })));
      const additionalHouseholdUtilities = [householdUtility];
      const expectedCollection: IHouseholdUtility[] = [...additionalHouseholdUtilities, ...householdUtilityCollection];
      jest.spyOn(householdUtilityService, 'addHouseholdUtilityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ consumptionHistory });
      comp.ngOnInit();

      expect(householdUtilityService.query).toHaveBeenCalled();
      expect(householdUtilityService.addHouseholdUtilityToCollectionIfMissing).toHaveBeenCalledWith(
        householdUtilityCollection,
        ...additionalHouseholdUtilities.map(expect.objectContaining),
      );
      expect(comp.householdUtilitiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const consumptionHistory: IConsumptionHistory = { id: 456 };
      const householdUtility: IHouseholdUtility = { id: 29669 };
      consumptionHistory.householdUtility = householdUtility;

      activatedRoute.data = of({ consumptionHistory });
      comp.ngOnInit();

      expect(comp.householdUtilitiesSharedCollection).toContain(householdUtility);
      expect(comp.consumptionHistory).toEqual(consumptionHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumptionHistory>>();
      const consumptionHistory = { id: 123 };
      jest.spyOn(consumptionHistoryFormService, 'getConsumptionHistory').mockReturnValue(consumptionHistory);
      jest.spyOn(consumptionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumptionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumptionHistory }));
      saveSubject.complete();

      // THEN
      expect(consumptionHistoryFormService.getConsumptionHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(consumptionHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(consumptionHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumptionHistory>>();
      const consumptionHistory = { id: 123 };
      jest.spyOn(consumptionHistoryFormService, 'getConsumptionHistory').mockReturnValue({ id: null });
      jest.spyOn(consumptionHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumptionHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: consumptionHistory }));
      saveSubject.complete();

      // THEN
      expect(consumptionHistoryFormService.getConsumptionHistory).toHaveBeenCalled();
      expect(consumptionHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConsumptionHistory>>();
      const consumptionHistory = { id: 123 };
      jest.spyOn(consumptionHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ consumptionHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(consumptionHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareHouseholdUtility', () => {
      it('Should forward to householdUtilityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(householdUtilityService, 'compareHouseholdUtility');
        comp.compareHouseholdUtility(entity, entity2);
        expect(householdUtilityService.compareHouseholdUtility).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
