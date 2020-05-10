import { element, by, ElementFinder } from 'protractor';

export class BuildingComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-building div table .btn-danger'));
  title = element.all(by.css('jhi-building div h2#page-heading span')).first();
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

export class BuildingUpdatePage {
  pageTitle = element(by.id('jhi-building-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  purchaseDateInput = element(by.id('field_purchaseDate'));
  propertyNumberInput = element(by.id('field_propertyNumber'));
  streetAddressInput = element(by.id('field_streetAddress'));
  cityInput = element(by.id('field_city'));
  stateCodeInput = element(by.id('field_stateCode'));
  zipCodeInput = element(by.id('field_zipCode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setPurchaseDateInput(purchaseDate: string): Promise<void> {
    await this.purchaseDateInput.sendKeys(purchaseDate);
  }

  async getPurchaseDateInput(): Promise<string> {
    return await this.purchaseDateInput.getAttribute('value');
  }

  async setPropertyNumberInput(propertyNumber: string): Promise<void> {
    await this.propertyNumberInput.sendKeys(propertyNumber);
  }

  async getPropertyNumberInput(): Promise<string> {
    return await this.propertyNumberInput.getAttribute('value');
  }

  async setStreetAddressInput(streetAddress: string): Promise<void> {
    await this.streetAddressInput.sendKeys(streetAddress);
  }

  async getStreetAddressInput(): Promise<string> {
    return await this.streetAddressInput.getAttribute('value');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setStateCodeInput(stateCode: string): Promise<void> {
    await this.stateCodeInput.sendKeys(stateCode);
  }

  async getStateCodeInput(): Promise<string> {
    return await this.stateCodeInput.getAttribute('value');
  }

  async setZipCodeInput(zipCode: string): Promise<void> {
    await this.zipCodeInput.sendKeys(zipCode);
  }

  async getZipCodeInput(): Promise<string> {
    return await this.zipCodeInput.getAttribute('value');
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

export class BuildingDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-building-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-building'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
