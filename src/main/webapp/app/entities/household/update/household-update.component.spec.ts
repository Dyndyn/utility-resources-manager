import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';
import { IHousehold } from '../household.model';
import { HouseholdService } from '../service/household.service';
import { HouseholdFormService } from './household-form.service';

import { HouseholdUpdateComponent } from './household-update.component';

describe('Household Management Update Component', () => {
  let comp: HouseholdUpdateComponent;
  let fixture: ComponentFixture<HouseholdUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let householdFormService: HouseholdFormService;
  let householdService: HouseholdService;
  let cityService: CityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), HouseholdUpdateComponent],
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
      .overrideTemplate(HouseholdUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HouseholdUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    householdFormService = TestBed.inject(HouseholdFormService);
    householdService = TestBed.inject(HouseholdService);
    cityService = TestBed.inject(CityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call City query and add missing value', () => {
      const household: IHousehold = { id: 456 };
      const city: ICity = { id: 32260 };
      household.city = city;

      const cityCollection: ICity[] = [{ id: 10893 }];
      jest.spyOn(cityService, 'query').mockReturnValue(of(new HttpResponse({ body: cityCollection })));
      const additionalCities = [city];
      const expectedCollection: ICity[] = [...additionalCities, ...cityCollection];
      jest.spyOn(cityService, 'addCityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ household });
      comp.ngOnInit();

      expect(cityService.query).toHaveBeenCalled();
      expect(cityService.addCityToCollectionIfMissing).toHaveBeenCalledWith(
        cityCollection,
        ...additionalCities.map(expect.objectContaining),
      );
      expect(comp.citiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const household: IHousehold = { id: 456 };
      const city: ICity = { id: 12488 };
      household.city = city;

      activatedRoute.data = of({ household });
      comp.ngOnInit();

      expect(comp.citiesSharedCollection).toContain(city);
      expect(comp.household).toEqual(household);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHousehold>>();
      const household = { id: 123 };
      jest.spyOn(householdFormService, 'getHousehold').mockReturnValue(household);
      jest.spyOn(householdService, 'partialUpdate').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ household });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: household }));
      saveSubject.complete();

      // THEN
      expect(householdFormService.getHousehold).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(householdService.partialUpdate).toHaveBeenCalledWith(expect.objectContaining(household));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHousehold>>();
      const household = { id: 123 };
      jest.spyOn(householdFormService, 'getHousehold').mockReturnValue({ id: null });
      jest.spyOn(householdService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ household: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: household }));
      saveSubject.complete();

      // THEN
      expect(householdFormService.getHousehold).toHaveBeenCalled();
      expect(householdService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHousehold>>();
      const household = { id: 123 };
      jest.spyOn(householdService, 'partialUpdate').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ household });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(householdService.partialUpdate).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCity', () => {
      it('Should forward to cityService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cityService, 'compareCity');
        comp.compareCity(entity, entity2);
        expect(cityService.compareCity).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
