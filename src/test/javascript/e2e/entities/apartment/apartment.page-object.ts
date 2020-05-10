import { element, by, ElementFinder } from 'protractor';

export class ApartmentComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-apartment div table .btn-danger'));
  title = element.all(by.css('jhi-apartment div h2#page-heading span')).first();
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

export class ApartmentUpdatePage {
  pageTitle = element(by.id('jhi-apartment-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  unitNumberInput = element(by.id('field_unitNumber'));
  moveInReadyInput = element(by.id('field_moveInReady'));

  buildingSelect = element(by.id('field_building'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setUnitNumberInput(unitNumber: string): Promise<void> {
    await this.unitNumberInput.sendKeys(unitNumber);
  }

  async getUnitNumberInput(): Promise<string> {
    return await this.unitNumberInput.getAttribute('value');
  }

  getMoveInReadyInput(): ElementFinder {
    return this.moveInReadyInput;
  }

  async buildingSelectLastOption(): Promise<void> {
    await this.buildingSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async buildingSelectOption(option: string): Promise<void> {
    await this.buildingSelect.sendKeys(option);
  }

  getBuildingSelect(): ElementFinder {
    return this.buildingSelect;
  }

  async getBuildingSelectedOption(): Promise<string> {
    return await this.buildingSelect.element(by.css('option:checked')).getText();
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

export class ApartmentDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-apartment-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-apartment'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
