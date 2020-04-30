import { element, by, ElementFinder } from 'protractor';

export class VehicleComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vehicle div table .btn-danger'));
  title = element.all(by.css('jhi-vehicle div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class VehicleUpdatePage {
  pageTitle = element(by.id('jhi-vehicle-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  makeInput = element(by.id('field_make'));
  modelInput = element(by.id('field_model'));
  modelYearInput = element(by.id('field_modelYear'));
  licensePlateNumberInput = element(by.id('field_licensePlateNumber'));
  licensePlateStateInput = element(by.id('field_licensePlateState'));
  reservedParkingStallNumberInput = element(by.id('field_reservedParkingStallNumber'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setMakeInput(make: string): Promise<void> {
    await this.makeInput.sendKeys(make);
  }

  async getMakeInput(): Promise<string> {
    return await this.makeInput.getAttribute('value');
  }

  async setModelInput(model: string): Promise<void> {
    await this.modelInput.sendKeys(model);
  }

  async getModelInput(): Promise<string> {
    return await this.modelInput.getAttribute('value');
  }

  async setModelYearInput(modelYear: string): Promise<void> {
    await this.modelYearInput.sendKeys(modelYear);
  }

  async getModelYearInput(): Promise<string> {
    return await this.modelYearInput.getAttribute('value');
  }

  async setLicensePlateNumberInput(licensePlateNumber: string): Promise<void> {
    await this.licensePlateNumberInput.sendKeys(licensePlateNumber);
  }

  async getLicensePlateNumberInput(): Promise<string> {
    return await this.licensePlateNumberInput.getAttribute('value');
  }

  async setLicensePlateStateInput(licensePlateState: string): Promise<void> {
    await this.licensePlateStateInput.sendKeys(licensePlateState);
  }

  async getLicensePlateStateInput(): Promise<string> {
    return await this.licensePlateStateInput.getAttribute('value');
  }

  async setReservedParkingStallNumberInput(reservedParkingStallNumber: string): Promise<void> {
    await this.reservedParkingStallNumberInput.sendKeys(reservedParkingStallNumber);
  }

  async getReservedParkingStallNumberInput(): Promise<string> {
    return await this.reservedParkingStallNumberInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class VehicleDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vehicle-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vehicle'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
